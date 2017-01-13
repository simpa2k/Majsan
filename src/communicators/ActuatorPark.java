package communicators;

import field.Environment;
import field.SoilMoistureDependentField;

import java.util.Map;

public class ActuatorPark {

    private Environment environment;

    public ActuatorPark(Environment environment){
        this.environment = environment;
    }

    public void actuate(Map<String, Double> actions){
        environment.affectSoilMoisture(actions.get(SoilMoistureDependentField.IRRIGATE));
    }
}
