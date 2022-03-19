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
    thresh = cv2.adaptiveThreshold(blurred, 255,cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY_INV, 21, 4)
    kernel = np.ones((13, 13))
    img_dilate = cv2.dilate(thresh, kernel, iterations=1)
    return cv2.erode(img_dilate, kernel, iterations=1)

def get_mask(img):
    contours, _ = cv2.findContours(process(img), cv2.RETR_TREE, cv2.CHAIN_APPROX_NONE)
    blank = np.zeros(img.shape[:2]).astype('uint8')
    for cnt in contours:
        if cv2.contourArea(cnt) > 700:
            peri = cv2.arcLength(cnt, True)
            approx = cv2.approxPolyDP(cnt, peri * 0.004, True)
            cv2.drawContours(blank, [approx], -1, 255, -1)
    return blank

