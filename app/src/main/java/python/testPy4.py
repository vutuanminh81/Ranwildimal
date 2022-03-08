import numpy as np
import cv2
from matplotlib import pyplot as plt
import sys
import os
from  PIL  import Image

def run(url, path):
    # Read image 
    image = cv2.imread(url).astype(np.uint8)

    # Get contours
    gray = (cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)).astype(np.uint8)
    img_canny = cv2.Canny(gray,50,150)
    ret,thresh = cv2.threshold(img_canny,
                           int(image[:, :, 0].mean()),
                           int(image[:, :, 1].mean()),
                           0)
    contours, _ = cv2.findContours(thresh, cv2.RETR_TREE,cv2.CHAIN_APPROX_SIMPLE)

    # Create mask
    mask = np.zeros(shape=(gray.shape), dtype=np.uint8)
    cv2.drawContours(mask, contours, -1, (1,0,0), cv2.FILLED)   
    mask = (~(mask == 1) * 1).astype(np.uint8)

    # cut off background
    img_masked = cv2.bitwise_and(image, image, mask=mask)
    cv2.imwrite(os.path.join(path , 'waka.jpg'), img_masked)
    cv2.imwrite(os.path.join(path , 'waka2.jpg'), mask)

