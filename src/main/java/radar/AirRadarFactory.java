package radar;

/**
 * Main "Factory" design pattern class, used to create {@link AirRadar}.
 *
 * @author Olgierd Królik
 */
public interface AirRadarFactory {
    /**
     * Factory-method creating instances of air quality radars.
     *
     * @return air quality radar instance depending on by which factory it was created
     */
    AirRadar createAirRadar();
}
