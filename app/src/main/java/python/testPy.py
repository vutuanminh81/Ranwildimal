import cv2
import numpy as np
import os

def run(url, path):
    img = cv2.imread(url)
    img_masked = cv2.bitwise_and(img, img, mask=getmask(img))
    cv2.imwrite(os.path.join(path , 'waka.jpg'), img_masked)

def process(img):
    img_gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    img_canny = cv2.Canny(img_gray, 50, 150)
    kernel = np.ones((13, 13))
    img_dilate = cv2.dilate(img_canny, kernel, iterations=1)
    return cv2.erode(img_dilate, kernel, iterations=1)

def getmask(img):
    contours, _ = cv2.findContours(process(img), cv2.RETR_TREE, cv2.CHAIN_APPROX_NONE)
    blank = np.zeros(img.shape[:2]).astype('uint8')
    for cnt in contours:
        if cv2.contourArea(cnt) > 700:
            peri = cv2.arcLength(cnt, True)
            approx = cv2.approxPolyDP(cnt, peri * 0.004, True)
            cv2.drawContours(blank, [approx], -1, 255, -1) 
    return blank
