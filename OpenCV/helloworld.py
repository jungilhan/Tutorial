#!/usr/bin/env python
# -*- coding: utf-8 -*-

from opencv.cv import *
from opencv.highgui import *

if __name__ == '__main__':
	image = cvLoadImage('sample.jpg')
	font = cvInitFont(CV_FONT_HERSHEY_SIMPLEX, 1, 1, 0, 2, 8)
	cvPutText(image, 'Hello World', (20, 40), font, (255, 255, 255))

	cvNamedWindow('Tutorial#1', CV_WINDOW_AUTOSIZE)
	cvShowImage('Tutorial#1', image)

	cvWaitKey(0)

	cvReleaseImage(image)
	cvDestroyWindow('Tutorial#1')

