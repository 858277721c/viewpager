/*
 * Copyright (C) 2017 zhengjun, fanwe (http://www.fanwe.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fanwe.lib.viewpager.indicator.model;

public class PositionData
{
    public int left;
    public int top;
    public int right;
    public int bottom;

    public int getWidth()
    {
        return right - left;
    }

    public int getHeight()
    {
        return bottom - top;
    }

    public int getLeftPercent(float percent)
    {
        return (int) (left + getWidth() * percent);
    }
}
