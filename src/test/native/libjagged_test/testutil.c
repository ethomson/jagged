#include <string.h>

void concat(char *dst, const char *src1, const char *src2) {
	strcpy_s(dst, 256, "");
	strcat_s(dst, 256, src1);
	strcat_s(dst, 256, "/");
	strcat_s(dst, 256, src2);
}
