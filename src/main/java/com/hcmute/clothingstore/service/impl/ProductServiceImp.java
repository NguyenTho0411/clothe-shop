package com.hcmute.clothingstore.service.impl;


import com.hcmute.clothingstore.anotationConfig.EnableSoftDeleteFilter;
import com.hcmute.clothingstore.dto.request.ProductDTO;
import com.hcmute.clothingstore.dto.response.PagninationResponse;
import com.hcmute.clothingstore.dto.response.ProductResponse;
import com.hcmute.clothingstore.entity.*;
import com.hcmute.clothingstore.enumerated.EColor;
import com.hcmute.clothingstore.enumerated.ESize;
import com.hcmute.clothingstore.enumerated.InventoryChangeType;
import com.hcmute.clothingstore.exception.APIException;
import com.hcmute.clothingstore.exception.ResouceAlreadyExist;
import com.hcmute.clothingstore.exception.ResourceNotFoundException;
import com.hcmute.clothingstore.repository.CategoryRepository;
import com.hcmute.clothingstore.repository.InventoryHistoryRepository;
import com.hcmute.clothingstore.repository.ProductRepository;
import com.hcmute.clothingstore.service.interfaces.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.awt.print.Pageable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private InventoryHistoryRepository inventoryHistoryRepository;

    @Override
    @EnableSoftDeleteFilter
    public PagninationResponse getProducts(Boolean isBestSeller, Boolean isDiscounted, Integer days, Double averageRating, Boolean hasDiscount, Double maxPrice, Double minPrice, String size, String sortOrder, Specification<Product> specification, Pageable pageable) {
        return null;
    }

    @Override
    public ProductResponse createProduct(ProductDTO productDTO) {
        String name = productDTO.getName();
        List<Product> existingProduct = productRepository.findByNameAndIsDeletedFalse(name);
        if (!existingProduct.isEmpty()) {
            throw new ResouceAlreadyExist("Product", "Name", name);
        }
        Product product = new Product();
        Long categoryID = productDTO.getCategoryId();
        Category category = categoryRepository.findById(categoryID).orElseThrow(() -> new ResourceNotFoundException("Category", "categoryID", categoryID));
        product.setCategory(category);
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(product.getPrice());
        product.setFeatured(productDTO.getFeatured());

        final Product finalProduct = product;
        product.setImages(IntStream.range(0, productDTO.getImages().size()).mapToObj(index -> {
            ProductImage productImage = new ProductImage();
            productImage.setPublicUrl(productDTO.getImages().get(index));
            productImage.setImageOrder(index);
            productImage.setProduct(finalProduct);
            return productImage;
        }).collect(Collectors.toList()));

        product.setProductVariants(productDTO.getVariants().stream().map(variant -> {
            ProductVariant productVariant = new ProductVariant();
            productVariant.setColor(EColor.valueOf(variant.getColor().toUpperCase()));
            productVariant.setSize(ESize.valueOf(variant.getSize().toUpperCase()));
            productVariant.setQuantity(variant.getQuantity());
            productVariant.setDifferentPrice(variant.getDifferentPrice());
            productVariant.setProduct(finalProduct);

            productVariant.setProductImageList(IntStream.range(0, variant.getImages().size()).mapToObj(index -> {
                ProductImage productImage = new ProductImage();
                productImage.setPublicUrl(variant.getImages().get(index));
                productImage.setImageOrder(index);
                productImage.setProductVariant(productVariant);
                return productImage;
            }).collect(Collectors.toList()));
            return productVariant;
        }).collect(Collectors.toList()));

        String slug = createSlug(productDTO.getName() + "-" + System.currentTimeMillis());
        product.setSlug(slug);

        productRepository.save(product);
        for (ProductVariant variant : product.getProductVariants()) {
            variant.setSku(generateMeaningfulSku(variant));

            Inventory inventory = new Inventory();
            inventory.setProductVariant(variant);
            inventory.setTimestamp(Instant.now());
            inventory.setNotes("khởi tạo số lượng ban đầu cho sản phẩm mới");
            inventory.setInventoryChangeType(InventoryChangeType.MANUAL);
            inventory.setChangeInQuantity(variant.getQuantity());
            inventory.setQuantityAfterChange(variant.getQuantity());
            inventoryHistoryRepository.save(inventory);
        }

        product = productRepository.save(product);
        return modelMapper.map(product, ProductResponse.class);
    }

    @Override
    public ProductResponse update(ProductDTO productDTO) {
        Long productId = productDTO.getId();
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Produdct"
                , "Product Id", productId));

        Long categoryId = productDTO.getCategoryId();
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        product.setCategory(category);
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setFeatured(product.isFeatured());
        product.setPrice(product.getPrice());
        String slug = createSlug(product.getName()) + "-" + System.currentTimeMillis();
        product.setSlug(slug);
        updateProductImage(product, productDTO.getImages());
        updateProductVariant(product, productDTO.getVariants());


        product = productRepository.save(product);
        return modelMapper.map(product, ProductResponse.class);
    }

    private void updateProductVariant(Product product, List<ProductDTO.ProductVariantDTO> variantDTO) {
        List<ProductVariant> currentVariant = new ArrayList<>(product.getProductVariants());
        List<ProductVariant> updatedVariant = new ArrayList<>();
        for (ProductDTO.ProductVariantDTO variant : variantDTO) {
            ProductVariant productVariant;
            if (variant.getId() != null) {
                productVariant = currentVariant.stream().filter(v -> v.getId().equals(variant.getId())).findFirst().
                        orElseThrow(() -> new ResourceNotFoundException("Product Variant", "Id", variant.getId()));

                currentVariant.removeIf(v -> v.getId().equals(variant.getId()));
            } else {
                productVariant = new ProductVariant();
                productVariant.setProduct(product);
            }

            productVariant.setSize(ESize.valueOf(variant.getSize().toUpperCase()));
            productVariant.setColor(EColor.valueOf(variant.getColor().toUpperCase()));
            productVariant.setDifferentPrice(variant.getDifferentPrice());
            updateProductVariantImage(productVariant, variant.getImages());

            updatedVariant.add(productVariant);
        }
        product.getProductVariants().clear();
        product.getProductVariants().addAll(updatedVariant);
    }

    private void updateProductVariantImage(ProductVariant productVariant, List<String> images) {
        if (productVariant.getProductImageList() == null) {
            productVariant.setProductImageList(new ArrayList<>());
        }
        List<ProductImage> imageVariantProduct = productVariant.getProductImageList();
        List<ProductImage> updatedProductImage = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            String imageUrl = images.get(i);
            ProductImage productImage = imageVariantProduct.stream().filter(img -> img.getPublicUrl().equals(imageUrl)).findFirst().orElseGet(() -> {
                ProductImage newImage = new ProductImage();
                newImage.setProductVariant(productVariant);
                return newImage;
            });

            productImage.setPublicUrl(imageUrl);
            productImage.setImageOrder(i);
            updatedProductImage.add(productImage);
        }
        imageVariantProduct.clear();
        imageVariantProduct.addAll(updatedProductImage);
    }

    private void updateProductImage(Product product, List<String> newImageurl) {
        List<ProductImage> currentImages = product.getImages();
        List<ProductImage> updatedProductImage = new ArrayList<>();

        for (int i = 0; i < newImageurl.size(); i++) {
            String imageUrl = newImageurl.get(i);
            ProductImage productImage = currentImages.stream().filter(img -> img.getPublicUrl().equals(imageUrl)).findFirst().orElseGet(ProductImage::new);
            productImage.setPublicUrl(imageUrl);
            productImage.setImageOrder(i);
            productImage.setProduct(product);
            updatedProductImage.add(productImage);
        }

        product.getImages().clear();
        product.getImages().addAll(updatedProductImage);
    }

    @Override
    public String generateMeaningfulSku(ProductVariant variant) {
        if (variant.getProduct() == null || variant.getProduct().getId() == null) {
            throw new APIException("Product and ProductID must be not null to generate sku.");
        }
        String productId = variant.getProduct().getId().toString();
        String productColor = variant.getColor().name().substring(0, Math.min(3, variant.getColor().name().length()));
        String productSize = variant.getSize().name();

        return String.format("%s-%s-%S", productId, productColor, productSize);
    }

    @Override
    @EnableSoftDeleteFilter
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product",
                "productId", id));
        return modelMapper.map(product, ProductResponse.class);
    }

    @Override
    @EnableSoftDeleteFilter
    public ProductResponse getProductBySlug(String slug) {
        Product product = productRepository.findBySlug(slug).orElseThrow(() -> new
                ResourceNotFoundException("Product", "Slug", slug));

        return modelMapper.map(product, ProductResponse.class);
    }

    @Override
    public String createSlug(String input) {
        // Replace Vietnamese characters with their non-accented equivalents
        String[] vietnameseChars = {"À", "Á", "Ạ", "Ả", "Ã", "Â", "Ầ", "Ấ", "Ậ", "Ẩ", "Ẫ", "Ă", "Ằ",
                "Ắ", "Ặ", "Ẳ", "Ẵ", "à", "á", "ạ", "ả", "ã", "â", "ầ", "ấ", "ậ", "ẩ", "ẫ", "ă", "ằ", "ắ",
                "ặ", "ẳ", "ẵ", "Đ", "đ", "È", "É", "Ẹ", "Ẻ", "Ẽ", "Ê", "Ề", "Ế", "Ệ", "Ể", "Ễ", "è", "é",
                "ẹ", "ẻ", "ẽ", "ê", "ề", "ế", "ệ", "ể", "ễ", "Ì", "Í", "Ị", "Ỉ", "Ĩ", "ì", "í", "ị", "ỉ",
                "ĩ", "Ò", "Ó", "Ọ", "Ỏ", "Õ", "Ô", "Ồ", "Ố", "Ộ", "Ổ", "Ỗ", "Ơ", "Ờ", "Ớ", "Ợ", "Ở", "Ỡ",
                "ò", "ó", "ọ", "ỏ", "õ", "ô", "ồ", "ố", "ộ", "ổ", "ỗ", "ơ", "ờ", "ớ", "ợ", "ở", "ỡ", "Ù",
                "Ú", "Ụ", "Ủ", "Ũ", "Ư", "Ừ", "Ứ", "Ự", "Ử", "Ữ", "ù", "ú", "ụ", "ủ", "ũ", "ư", "ừ", "ứ",
                "ự", "ử", "ữ", "Ỳ", "Ý", "Ỵ", "Ỷ", "Ỹ", "ỳ", "ý", "ỵ", "ỷ", "ỹ"};
        String[] nonAccentChars = {"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
                "A", "A", "A", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a",
                "a", "a", "D", "d", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "e", "e", "e",
                "e", "e", "e", "e", "e", "e", "e", "e", "I", "I", "I", "I", "I", "i", "i", "i", "i", "i",
                "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "o",
                "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "U", "U",
                "U", "U", "U", "U", "U", "U", "U", "U", "U", "u", "u", "u", "u", "u", "u", "u", "u", "u",
                "u", "u", "Y", "Y", "Y", "Y", "Y", "y", "y", "y", "y", "y"};

        for (int i = 0; i < vietnameseChars.length; i++) {
            input = input.replace(vietnameseChars[i], nonAccentChars[i]);
        }

        input = input.toLowerCase();

        input = input.replaceAll("[^a-z0-9\\s-]", "");

        input = input.trim().replaceAll("\\s+", "-").replaceAll("-+", "-");

        return input.isEmpty() ? "product" : input;
    }
}
