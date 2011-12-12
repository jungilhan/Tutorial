#include "apDebugSink.h"
#include <time.h>

apDebugSink::apDebugSink () : enableHeader_(false) {}

std::string apDebugSink::standardHeader()
{
	std::string header;
	
	// 현재 시간을 가져옵니다.
	time_t now = time(0);
	header += ctime(&now);
	header.erase(header.length() - 1, 1); // 개행 문자를 제거합니다.
	header += ": ";
	
	return header;
}

