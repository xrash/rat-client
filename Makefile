debug :
	ant debug

install : debug
	adb install -r bin/Rat-debug.apk
