package it.me.domain;

import lombok.Generated;

/**
 * <h1>Costanti Portfolio</h1>
 * <p>Suddivise in sotto classi in base all'utilizzo:
 * <ul>
 *     <li>{@link PortfolioPublicK.Github}</li>
 *     <li>{@link PortfolioPublicK.CacheTTL}</li>
 * </ul></p>
 */
@Generated
public final class PortfolioPublicK {
    /**
     * <h2>Costanti per Github</h2>
     * <p>Attualmente mappate:
     * <ul>
     *     <li>{@link PortfolioPublicK.Github#PORTFOLIO_URL}</li>
     * </ul></p>
     *
     * @implNote child of {@link PortfolioPublicK}
     */
    public static final class Github {
        public static final String PORTFOLIO_URL = "https://github.com/ciamb/portfolio";
    }

    /**
     * <h2>Costanti per cache</h2>
     * <p>Attualmente mappate:
     * <ul>
     *     <li>{@link PortfolioPublicK.CacheTTL#TEN_MINUTE}</li>
     * </ul></p>
     *
     * @implNote child of {@link PortfolioPublicK}
     * @since 1.3.5
     */
    public static final class CacheTTL {
        public static final long TEN_MINUTE = 600_000;
    }
}
