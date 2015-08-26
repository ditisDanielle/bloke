/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.builder;

import java.math.BigDecimal;

import nl.mad.bacchus.model.Wine;
import nl.mad.bacchus.model.meta.WineRegion;
import nl.mad.bacchus.model.meta.WineType;

import org.springframework.stereotype.Component;

/**
 * Builder of wines.
 *
 * @author Jeroen van Schagen
 * @since Apr 13, 2015
 */
@Component
public class WineBuilder extends AbstractBuilder {

    /**
     * Start building a custom wine.
     * 
     * @return the build command
     */
    public WineBuildCommand newWine() {
        return new WineBuildCommand();
    }

    /**
     * Allows building custom wines.
     *
     * @author Jeroen van Schagen
     * @since Apr 14, 2015
     */
    public class WineBuildCommand {
        
        private final Wine wine = new Wine();

        /**
         * Changes the name of our wine.
         * @param name the new name
         * @return this instance, for chaining
         */
        public WineBuildCommand withName(String name) {
            wine.setName(name);
            return this;
        }
        
        /**
         * Changes the cost of our wine.
         * @param cost the new cost
         * @return this instance, for chaining
         */
        public WineBuildCommand withCost(BigDecimal cost) {
            wine.setCost(cost);
            return this;
        }
        
        /**
         * Changes the specs of the wine.
         * @param type the type of wine.
         * @param region the wine region.
         * @param year the year of harvest.
         * @return the builder.
         */
        public WineBuildCommand withSpecs(WineType type,WineRegion region, Integer year) {
        	wine.setYear(year);
        	return withSpecs(type, region);
        }

        /**
         * Changes the specs of the wine.
         * @param type the type of wine.
         * @param region the wine region.
         * @return the builder.
         */
		public WineBuildCommand withSpecs(WineType type, WineRegion region) {
        	wine.setWineType(type);
        	wine.setRegion(region);
        	wine.setCountry(region.getCountry());
			return this;
		}


        /**
         * Build the wine.
         * @return the created wine
         */
        public Wine build() {
            return wine;
        }

        /**
         * Persists the wine.
         * @return the persisted wine
         */
        public Wine save() {
            return saveWithTransaction(build());
        }

    }

}
