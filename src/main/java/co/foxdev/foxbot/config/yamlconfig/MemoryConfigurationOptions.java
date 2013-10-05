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
 * Various settings for controlling the input and output of a
 * {@link MemoryConfiguration}
 */
public class MemoryConfigurationOptions extends ConfigurationOptions
{

    protected MemoryConfigurationOptions(MemoryConfiguration configuration)
    {
        super(configuration);
    }

    @Override
    public MemoryConfiguration configuration()
    {
        return (MemoryConfiguration) super.configuration();
    }

    @Override
    public MemoryConfigurationOptions copyDefaults(boolean value)
    {
        super.copyDefaults(value);
        return this;
    }

    @Override
    public MemoryConfigurationOptions pathSeparator(char value)
    {
        super.pathSeparator(value);
        return this;
    }
}
