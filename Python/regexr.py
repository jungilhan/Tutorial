#!/usr/bin/env python
# -*- coding: utf-8 -*-

import re

if __name__ == '__main__':
	text ='03:20:10 access("/var/lib/update-notifier/user.d/firefox-restart-required", F_OK) = -1 ENOENT (No such file or directory)'
	matches = re.findall(r'\"(.+?)\"', text)
	print matches

	text = 'open("libc.so.6", O_RDONLY)             = -1 ENOENT (No such file or directory)'
	matches = re.findall('.+?\(', text)
	func = matches[0]
	print func[0:len(func) - 1]

	text = 'mmap2(NULL, 8192, PROT_READ|PROT_WRITE, MAP_PRIVATE|MAP_ANONYMOUS, -1, 0) = 0xb76ef000'
	matches = re.findall(r'\b[A-Z]+\b|\b[A-Z]+_[A-Z]+\b', text)
	print matches
