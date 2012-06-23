#!/usr/bin/env python
# -*- coding: utf-8 -*-

from opencv.cv import *
from opencv.highgui import *
from opencv import *

if __name__ == '__main__':
	source = cvLoadImage('simpson.jpg')
	template = cvLoadImage('milhouse.jpg')

	width = source.width - template.width + 1
	height = source.height - template.height + 1
	result = cvCreateImage(cvSize(width, height), 32, 1)

	mathod = CV_TM_SQDIFF
	#mathod = CV_TM_SQDIFF_NORMED
	#mathod = CV_TM_CCORR
	#mathod = CV_TM_CCORR_NORMED
	#mathod = CV_TM_CCOEFF
	#mathod = CV_TM_CCOEFF_NORMED
	cvMatchTemplate(source, template, result, mathod)

	minval, maxval, minloc, maxloc = cvMinMaxLoc(result, 0)
	
	matchloc = cvPoint(0, 0)
	if mathod == CV_TM_SQDIFF or mathod == CV_TM_SQDIFF_NORMED:
		matchloc = minloc
	else:
		matchloc = maxloc

	cvRectangle(source, cvPoint(matchloc.x, matchloc.y), cvPoint(matchloc.x + template.width, matchloc.y + template.height), CV_RGB(255, 0, 0))

	cvNamedWindow('Source', CV_WINDOW_AUTOSIZE)
	cvNamedWindow('Result', CV_WINDOW_AUTOSIZE)	

	cvShowImage('Source', source)
	cvShowImage('Result', result)

	cvWaitKey(0)

	cvReleaseImage(source)
	cvReleaseImage(template)
	cvReleaseImage(result)

	cvDestroyWindow('Source')
	cvDestroyWindow('Result')
