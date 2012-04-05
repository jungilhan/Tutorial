# 소스 파일들의 위치를 알려줌.
LOCAL_PATH := $(call my-dir)

# Make 관련 환경 변수를 초기화.
include $(CLEAR_VARS)

# 라이브러리를 빌드하기 위한 정보 생성(라이브러리 이름, 소스 코드 등)
LOCAL_MODULE := hello
LOCAL_SRC_FILES	:= hello.cpp

# 공유 라이브러리 생성
include $(BUILD_SHARED_LIBRARY)

