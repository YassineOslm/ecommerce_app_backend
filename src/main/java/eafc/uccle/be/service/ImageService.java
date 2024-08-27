package eafc.uccle.be.service;

import eafc.uccle.be.dao.ImageRepository;
import eafc.uccle.be.entity.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
public class ImageService {

    @Autowired
    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Set<Image> getImagesByProductId(Long productId) {
        return imageRepository.findByProductId(productId);
    }
}

