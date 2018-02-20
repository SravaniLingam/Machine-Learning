# install.packages("jpeg",dependencies = TRUE)
# install.packages("ggplot2",dependencies = TRUE)
library(jpeg)
library(ggplot2)
img1 = "image1.jpg"
img2 = "image2.jpg"
img3 = "image3.jpg"
img4 = "image4.jpg"
img5 = "image5.jpg"

images <- c(img1, img2, img3, img4, img5)
# images <- c("image1.jpg")
# print(images)
for(img in images){
  img_name = img;
  img = readJPEG(img)
  # print(img)
  dimensions <- dim(img)
  img_rgb <- data.frame(
    x = rep(1:dimensions[2], each = dimensions[1]),
    y = rep(dimensions[1]:1, dimensions[2]),
    R = as.vector(img[,,1]),
    G = as.vector(img[,,2]),
    B = as.vector(img[,,3])
    )

    ggplot(data = img_rgb, aes(x = x, y = y)) +
        geom_point(colour = rgb(img_rgb[c("R", "G", "B")])) +
        labs(title = "Original Image") +
        xlab("x") +
        ylab("y")

    k <- 2
    kmeans_img <- kmeans(img_rgb[,c("R","G","B")], centers = k, iter.max = 5, nstart = 3)
    clustered_img <- rgb(kmeans_img$centers[kmeans_img$cluster,])

    diag_path = file.path(getwd(),"Clustered_Images",paste(k,"-clustered-",img_name,".jpeg",sep=""))

    jpeg(file=diag_path)

    pl <- ggplot(data = img_rgb, aes(x = x, y = y)) +
      geom_point(colour = clustered_img) +
      labs(title = paste(k, "color cluster on ",img)) +
      xlab("x") +
      ylab("y")

    plot(pl)
    dev.off()

}
