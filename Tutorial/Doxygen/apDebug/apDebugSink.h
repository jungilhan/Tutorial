/** @file apDebugSink.h 파일은 디버깅 스트림을 위한 추상 클래스를 정의한 파일입니다. */

#include <string>

/**
* apDebugSink 클래스는 디버깅 스트림을 위한  인터페이스입니다. 
*/
class apDebugSink {
public:
	apDebugSink ();
	
	/**
	* 디버깅 싱크에 문자열을 씁니다.
	*/
	virtual void write(const std::string str) = 0;

	/**
	* 디버깅 싱크에 문자를 씁니다.
	*/
	virtual void write(int c) = 0;
	
	/**
	* 저장된 모든 정보를 내보냅니다.
	*/
	virtual void flush() {}

	/**
	* 기본적으로 헤더들이 사용 가능해졌을 때 표준 헤더를 내보냅니다.
	*/	
	virtual std::string header() { return standardHeader(); }


	/**
	* 헤더를 기록할 것인지를 정합니다.
	* @param state 디버깅 스트림 출력 시 헤더 출력 여부 설정.
	*/
	void showHeader(bool state) { enableHeader_ = state; }
	
protected:
	/**
	* header()를 재정의하지 않았을do 때 기본적인 헤더를 제공하는 함수입니다. 
	* @return std::string 현재 시간을 반환.
	*/
	std::string standardHeader();
	
	bool enableHeader_;	/**< true이면 버퍼를 flush 할 때 헤더를 같이 기록합니다.  */
};
