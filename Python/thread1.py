#!/usr/bin/env python
# -*- coding: utf-8 -*-

import threading, time

def myThread(id):
	for i in range(10):
		print 'id=%s --> %s' % (id, i)
		time.sleep(0)

threads = []
for i in range(2):
	th = threading.Thread(target=myThread, args=(i,))
	th.start()
	threads.append(th)

for th in threads:
	th.join()
print 'Exiting'
