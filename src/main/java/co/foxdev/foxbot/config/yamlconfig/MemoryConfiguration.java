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

import com.google.common.base.Preconditions;

import java.util.Map;

/**
 * This is a {@link Configuration} implementation that does not save or load
 * from any source, and stores all values in memory only. This is useful for
 * temporary Configurations for providing defaults.
 */
public class MemoryConfiguration extends MemorySection implements Configuration
{

    protected Configuration defaults;
    protected MemoryConfigurationOptions options;

    /**
     * Creates an empty {@link MemoryConfiguration} with no default values.
     */
    public MemoryConfiguration()
    {
    }

    /**
     * Creates an empty {@link MemoryConfiguration} using the specified
     * {@link Configuration} as a source for all default values.
     *
     * @param defaults Default value provider
     * @throws IllegalArgumentException Thrown if defaults is null
     */
    public MemoryConfiguration(Configuration defaults)
    {
        this.defaults = defaults;
    }

    @Override
    public void addDefault(String path, Object value)
    {
        Preconditions.checkNotNull(path, "Path may not be null");

        if (defaults == null)
        {
            defaults = new MemoryConfiguration();
        }

        defaults.set(path, value);
    }

    public void addDefaults(Map<String, Object> defaults)
    {
        Preconditions.checkNotNull(defaults, "Defaults may not be null");

        for (Map.Entry<String, Object> entry : defaults.entrySet())
        {
            addDefault(entry.getKey(), entry.getValue());
        }
    }

    public void addDefaults(Configuration defaults)
    {
        Preconditions.checkNotNull(defaults, "Defaults may not be null");
        addDefaults(defaults.getValues(true));
    }

    public void setDefaults(Configuration defaults)
    {
        Preconditions.checkNotNull(defaults, "Defaults may not be null");
        this.defaults = defaults;
    }

    public Configuration getDefaults()
    {
        return defaults;
    }

    @Override
    public ConfigurationSection getParent()
    {
        return null;
    }

    public MemoryConfigurationOptions options()
    {
        if (options == null)
        {
            options = new MemoryConfigurationOptions(this);
        }

        return options;
    }
}
