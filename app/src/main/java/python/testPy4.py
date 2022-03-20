import numpy as np
import cv2
from matplotlib import pyplot as plt
import sys
import os
from  PIL  import Image

def run(url, path, filename):
    # Read image 
    image = cv2.imread(url).astype(np.uint8)

    mask1 = get_mask(image)
    img_masked = cv2.bitwise_and(image, image, mask=mask1)

    cv2.imwrite(os.path.join(path , filename), img_masked)

def process(img):
    # convert the image to grayscale and blur it slightly
    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    blurred = cv2.GaussianBlur(gray, (7, 7), 0)
    (T, threshInv) = cv2.threshold(blurred, 0, 255,cv2.THRESH_BINARY_INV | cv2.THRESH_OTSU)
    #auto = auto_canny(blurred)
    kernel = np.ones((25, 25))

    img_dilate = cv2.dilate(threshInv, kernel, iterations=1)
    img3=cv2.morphologyEx(img_dilate, cv2.MORPH_CLOSE, kernel)
    #img_erode = cv2.erode(img_dilate, kernel, iterations=2)
    return img3

def get_mask(img):
    contours, _ = cv2.findContours(process(img), cv2.RETR_TREE, cv2.CHAIN_APPROX_NONE)
    blank = np.zeros(img.shape[:2]).astype('uint8')
    for cnt in contours:
        if cv2.contourArea(cnt) > 700:
            peri = cv2.arcLength(cnt, True)
            approx = cv2.approxPolyDP(cnt, peri * 0.004, True)
            cv2.drawContours(blank, [approx], -1, 255, -1)
    return blank

