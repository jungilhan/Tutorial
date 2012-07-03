#!/usr/bin/env python
# -*- coding: utf-8 -*-

import threading, time

class MyThread(threading.Thread):
	def run(self):
		for i in range(10):
			print 'id=%s --> %s' % (self.getName(), i)
			time.sleep(0)

threads = []
for i in range(2):
	th = MyThread()
	th.start()
	threads.append(th)

for th in threads:
	th.join()
print 'Exiting'
