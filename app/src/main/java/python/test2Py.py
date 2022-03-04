import cv2
import numpy as np
from matplotlib import pyplot as plt
import os

def run(url, path):
    image_bgr = cv2.imread(url)
    image_rgb = cv2.cvtColor(image_bgr, cv2.COLOR_BGR2RGB)

    rectangle = (600, 600, 1500, 1500)
    # 600, 550, 1150, 2000
    mask = np.zeros(image_rgb.shape[:2], np.uint8)

    bgdModel = np.zeros((1, 65), np.float64)
    fgdModel = np.zeros((1, 65), np.float64)

    cv2.grabCut(image_rgb, mask, rectangle, bgdModel, fgdModel, 5, cv2.GC_INIT_WITH_RECT)

    mask_2 = np.where((mask == 2) | (mask == 0), 0, 1).astype('uint8')

    image_rgd_nobg = image_rgb * mask_2[:, :, np.newaxis]
    cv2.imwrite(os.path.join(path , 'waka.jpg'), image_rgd_nobg)
