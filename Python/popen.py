#!/usr/bin/env python
# -*- coding: utf-8 -*-

import subprocess
import time
import threading
import fcntl
import os

stop = False

def job():
	process = subprocess.Popen(['tail', '-f', '/var/log/syslog'], stdout=subprocess.PIPE)
	fd = process.stdout.fileno()
	fl = fcntl.fcntl(fd, fcntl.F_GETFL)
	fcntl.fcntl(fd, fcntl.F_SETFL, fl | os.O_NONBLOCK)

	while True:
		if stop == True:
			process.kill()

		poll = process.poll()
		if poll != None:
			break

		try:
			print process.stdout.readline().strip()
		except IOError:
			pass

if __name__ == '__main__':
	thread = threading.Thread(target=job, args=())
	thread.start()

	time.sleep(5)
	stop = True
	thread.join()
	print 'Exit'
