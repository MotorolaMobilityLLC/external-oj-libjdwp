/*
 * Copyright (c) 2001, 2008, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

#ifndef AWT_WIN32GRAPHICSDEVICE_H
#define AWT_WIN32GRAPHICSDEVICE_H

#include "awt.h"
extern "C" {
    #include "img_globals.h"
} // extern "C"
#include "colordata.h"
#include "awt_Palette.h"
#include "Devices.h"

struct RECT_BOUNDS {
    int x;
    int y;
    int width;
    int height;
    RECT_BOUNDS(int _x, int _y, int _w, int _h) : x(_x), y(_y), width(_w), height(_h) {}
};

class AwtPalette;
class Devices;

class AwtWin32GraphicsDevice {
public:
                            AwtWin32GraphicsDevice(int screen, HMONITOR mhnd, Devices *arr);
                            ~AwtWin32GraphicsDevice();
    void                    UpdateDeviceColorState();
    void                    SetGrayness(int grayValue);
    int                     GetGrayness() { return colorData->grayscale; }
    HDC                     GetDC();
    void                    ReleaseDC(HDC hDC);
    jobject                 GetColorModel(JNIEnv *env,
                                          jboolean useDeviceSettings);
    void                    Initialize();
    void                    UpdateDynamicColorModel();
    BOOL                    UpdateSystemPalette();
    unsigned int            *GetSystemPaletteEntries();
    unsigned char           *GetSystemInverseLUT();
    void                    SetJavaDevice(JNIEnv *env, jobject objPtr);
    HPALETTE                SelectPalette(HDC hDC);
    void                    RealizePalette(HDC hDC);
    HPALETTE                GetPalette();
    ColorData               *GetColorData() { return cData; }
    int                     GetBitDepth() { return colorData->bitsperpixel; }
    HMONITOR                GetMonitor() { return monitor; }
    LPMONITORINFO           GetMonitorInfo() { return pMonitorInfo; }
    jobject                 GetJavaDevice() { return javaDevice; }
    int                     GetDeviceIndex() { return screen; }
    void                    Release();
    void                    DisableOffscreenAcceleration();
    void                    Invalidate(JNIEnv *env);
    void                    InitDesktopScales(bool fractionalScaleEnabled);
    void                    SetScale(float scaleX, float scaleY);
    float                   GetScaleX();
    float                   GetScaleY();
    int                     ScaleUpX(int x);
    int                     ScaleUpY(int y);
    int                     ScaleDownX(int x);
    int                     ScaleDownY(int y);
    int                     ScaleUpDX(int x);
    int                     ScaleUpDY(int y);
    int                     ScaleDownDX(int x);
    int                     ScaleDownDY(int y);

    static void             ScaleDownDPoint(POINT *pt);
    static int              DeviceIndexForWindow(HWND hWnd);
    static jobject          GetColorModel(JNIEnv *env, jboolean dynamic,
                                          int deviceIndex);
    static HPALETTE         SelectPalette(HDC hDC, int deviceIndex);
    static void             RealizePalette(HDC hDC, int deviceIndex);
    static ColorData        *GetColorData(int deviceIndex);
    static int              GetGrayness(int deviceIndex);
    static void             UpdateDynamicColorModel(int deviceIndex);
    static BOOL             UpdateSystemPalette(int deviceIndex);
    static HPALETTE         GetPalette(int deviceIndex);
    static HMONITOR         GetMonitor(int deviceIndex);
    static LPMONITORINFO    GetMonitorInfo(int deviceIndex);
    static void             ResetAllMonitorInfo();
    static BOOL             IsPrimaryPalettized() { return primaryPalettized; }
    static int              GetDefaultDeviceIndex() { return primaryIndex; }
    static void             DisableOffscreenAccelerationForDevice(HMONITOR hMonitor);
    static HDC              GetDCFromScreen(int screen);
    static int              GetScreenFromHMONITOR(HMONITOR mon);
    static BOOL             IsUiScaleEnabled(); // if not, be dpi-unaware (backward compatible behaviour)
    static AwtWin32GraphicsDevice* GetDeviceByBounds(RECT_BOUNDS bounds , HWND hwnd = NULL); // bounds in user space

    inline static RECT_BOUNDS GetWindowRect(HWND hwnd)
    {
        RECT r;
        ::GetWindowRect(hwnd, &r);
        return RECT_BOUNDS(r.left, r.top, r.right - r.left, r.bottom - r.top);
    }

    static int              primaryIndex;
    static BOOL             primaryPalettized;
    static jclass           indexCMClass;
    static jclass           wToolkitClass;
    static jfieldID         dynamicColorModelID;
    static jfieldID         indexCMrgbID;
    static jfieldID         indexCMcacheID;
    static jmethodID        paletteChangedMID;

private:
    static BOOL             AreSameMonitors(HMONITOR mon1, HMONITOR mon2);
    ImgColorData            *colorData;
    AwtPalette              *palette;
    ColorData               *cData;     // Could be static, but may sometime
                                        // have per-device info in this structure
    BITMAPINFO              *gpBitmapInfo;
    int                     screen;
    HMONITOR                monitor;
    LPMONITORINFO           pMonitorInfo;
    jobject                 javaDevice;
    Devices                 *devicesArray;
    float                   scaleX;
    float                   scaleY;

    static HDC              MakeDCFromMonitor(HMONITOR);
};

#endif AWT_WIN32GRAPHICSDEVICE_H
