/*
 * This file is part of Foxbot.
 *
 *     Foxbot is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foxbot is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Foxbot. If not, see <http://www.gnu.org/licenses/>.
 */

package co.foxdev.foxbot.config.yamlconfig;

/**
 * Utils for casting number types to other number types
 */
public final class NumberConversions
{

    private NumberConversions()
    {
    }

    public static int floor(double num)
    {
        final int floor = (int) num;
        return floor == num ? floor : floor - (int) (Double.doubleToRawLongBits(num) >>> 63);
    }

    public static int ceil(final double num)
    {
        final int floor = (int) num;
        return floor == num ? floor : floor + (int) (~Double.doubleToRawLongBits(num) >>> 63);
    }

    public static int round(double num)
    {
        return floor(num + 0.5d);
    }

    public static int toInt(Object object)
    {
        if (object instanceof Number)
        {
            return ((Number) object).intValue();
        }

        try
        {
            return Integer.valueOf(object.toString());
        }
        catch (NumberFormatException e)
        {
        }
        catch (NullPointerException e)
        {
        }
        return 0;
    }

    public static float toFloat(Object object)
    {
        if (object instanceof Number)
        {
            return ((Number) object).floatValue();
        }

        try
        {
            return Float.valueOf(object.toString());
        }
        catch (NumberFormatException e)
        {
        }
        catch (NullPointerException e)
        {
        }
        return 0;
    }

    public static double toDouble(Object object)
    {
        if (object instanceof Number)
        {
            return ((Number) object).doubleValue();
        }

        try
        {
            return Double.valueOf(object.toString());
        }
        catch (NumberFormatException e)
        {
        }
        catch (NullPointerException e)
        {
        }
        return 0;
    }

    public static long toLong(Object object)
    {
        if (object instanceof Number)
        {
            return ((Number) object).longValue();
        }

        try
        {
            return Long.valueOf(object.toString());
        }
        catch (NumberFormatException e)
        {
        }
        catch (NullPointerException e)
        {
        }
        return 0;
    }

    public static short toShort(Object object)
    {
        if (object instanceof Number)
        {
            return ((Number) object).shortValue();
        }

        try
        {
            return Short.valueOf(object.toString());
        }
        catch (NumberFormatException e)
        {
        }
        catch (NullPointerException e)
        {
        }
        return 0;
    }

    public static byte toByte(Object object)
    {
        if (object instanceof Number)
        {
            return ((Number) object).byteValue();
        }

        try
        {
            return Byte.valueOf(object.toString());
        }
        catch (NumberFormatException e)
        {
        }
        catch (NullPointerException e)
        {
        }
        return 0;
    }
}
