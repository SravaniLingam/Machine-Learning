import numpy as np
import sys
from sklearn.decomposition import PCA
import matplotlib.pyplot as plt
from pylab import *
from skimage import data, io, color
## Source Code : https://pakallis.wordpress.com/2013/06/20/principal-component-analysis-in-an-image-with-scikit-learn-and-scikit-image/
args = sys.argv
link = args[1]
image_gray = io.imread(link,as_gray=True)
subplot(2, 2, 1)
io.imshow(image_gray)
xlabel('Original Image')
for i in range(1, 4):
    n_comp = 3 ** i
    pca = PCA(n_components = n_comp)
    pca.fit(image_gray)
    image_gray_pca = pca.fit_transform(image_gray)
    # subplot(2, 2, 2)
    # io.imshow(coke_gray_pca)
    # xlabel('Image after applying PCA')
    image_gray_restored = pca.inverse_transform(image_gray_pca)
    subplot(2, 2, i+1)
    io.imshow(image_gray_restored)
    xlabel('Restored image n_components = %s' %n_comp)
    print ('Variance retained for %s components %s is  %%' %(n_comp, (1 - sum(pca.explained_variance_ratio_) / size(pca.explained_variance_ratio_)) * 100))
    print ('Compression Ratio for %s components is %s %%' %(n_comp, (float(size(image_gray_pca)) / size(image_gray) * 100)))
show()
