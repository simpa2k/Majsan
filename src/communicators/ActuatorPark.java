package communicators;

import field.Environment;
import field.SoilMoistureDependentField;

import java.util.Map;

/**
 * Created by Maja on 2016-12-13.
 */
public class ActuatorPark {

    private Environment environment;

    public ActuatorPark(Environment environment){
        this.environment = environment;
    }

    public void actuate(Map<String, Double> actions){
        environment.affectSoilMoisture(actions.get(SoilMoistureDependentField.IRRIGATE));
    }
}
