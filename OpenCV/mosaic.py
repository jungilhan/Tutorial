#!/usr/bin/env python
# -*- coding: utf-8 -*-

from opencv.cv import *
from opencv.highgui import *
from opencv import *

if __name__ == '__main__':
	image = cvLoadImage('fruits.jpg')
	dotImage = cvCreateImage(cvGetSize(image), image.depth, image.nChannels)

	cvNamedWindow('Mosaic Source', CV_WINDOW_AUTOSIZE)
	cvNamedWindow('Mosaic Output', CV_WINDOW_AUTOSIZE)

	cvShowImage('Mosaic Source', image)

	tileCols = 64
	tileRows = 60
	tileWidth = image.width / tileCols
	tileHeight = image.height / tileRows

	for y in range(tileRows):
		for x in range(tileCols):
			color = cvGet2D(image, (y * tileHeight) + (tileHeight / 2), (x * tileWidth) + (tileWidth / 2))
			cvRectangle(dotImage, (x * tileWidth, y * tileHeight), (x * tileWidth + tileWidth, y * tileHeight + tileHeight), (0, 0, 0))
			cvRectangle(dotImage, (x * tileWidth + 1, y * tileHeight + 1), (x * tileWidth + tileWidth - 1, y * tileHeight + tileHeight - 1), color, CV_FILLED)
			#cvCircle(dotImage, ((x * tileWidth) + (tileWidth / 2), (y * tileHeight) + (tileHeight / 2)), tileWidth / 2, color, CV_FILLED)

	cvShowImage('Mosaic Output', dotImage)

	cvWaitKey(0)

	cvReleaseImage(image)
	cvReleaseImage(dotImage)
	cvDestroyWindow('Mosaic Source')
	cvDestroyWindow('Mosaic Output')
