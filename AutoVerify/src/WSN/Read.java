package WSN;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
public class Read 
{
	public ArrayList<Object> readKwsn(String filePath)
	{
		try {
			     	
            JAXBContext context = JAXBContext.newInstance(Wsn.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Wsn wsn = (Wsn) unmarshaller.unmarshal(new File(filePath));
            //HashMap<String , Object> kwsnData = new HashMap<>();
            ArrayList<Object> kwsnData = new ArrayList<>();
            processData(kwsnData,wsn); 
            return kwsnData;

        } 
		catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
	}
	
	public static void processData (ArrayList<Object> data,Wsn wsn) {

        data.add(wsn.network.SensorMaxBufferSize);
                
        data.add(wsn.network.SensorMaxQueueSize);
                
        data.add(wsn.network.ChannelMaxBufferSize);
                
        data.add(wsn.network.processes.get(0).sensors.listSensor.get(0).MaxProcessingRate);
                
        data.add(wsn.network.processes.get(0).sensors.listSensor.get(0).MaxSendingRate);
                
        data.add(wsn.network.processes.get(0).links.listLink.get(0).MaxSendingRate);
                
        data.add(wsn.network.processes.get(0).sensors.listSensor);
                
        data.add(wsn.network.processes.get(0).links.listLink);
                
        data.add(wsn.network.NumberOfPacket);
        data.add("1");
        data.add("1");
        data.add("1");
        ArrayList<String> energyRule = new ArrayList<>();
        energyRule.add("5");
        energyRule.add("5");
        energyRule.add("5");

        data.add(energyRule);
        System.out.println(data);
        System.out.println("No problems then");
    }
 
@SuppressWarnings("unused")
private static void processData2 (HashMap<String ,Object> data,Wsn wsn) {

        data.put(TopologyConstants.SENSORS_MAX_BUFFER_SIZE_KEY,
                wsn.network.SensorMaxBufferSize);
        data.put(TopologyConstants.SENSORS_MAX_QUEUE_SIZE_KEY,
                wsn.network.SensorMaxQueueSize);
        data.put(TopologyConstants.CHANEL_MAX_BUFFER_SIZE_KEY,
                wsn.network.ChannelMaxBufferSize);
        data.put(TopologyConstants.SENSORS_MAX_PROCESSING_RATE_KEY,
                wsn.network.processes.get(0).sensors.listSensor.get(0).MaxProcessingRate);
        data.put(TopologyConstants.SENSORS_MAX_SENDING_RATE_KEY,
                wsn.network.processes.get(0).sensors.listSensor.get(0).MaxSendingRate);
        data.put(TopologyConstants.CHANNEL_MAX_SENDING_RATE_KEY,
                wsn.network.processes.get(0).links.listLink.get(0).MaxSendingRate);
        data.put(TopologyConstants.SENSORS_LIST_KEY,
                wsn.network.processes.get(0).sensors.listSensor);
        data.put(TopologyConstants.CHANNEL_LIST_KEY,
                wsn.network.processes.get(0).links.listLink);
        data.put(TopologyConstants.NUMBER_OF_PACKAGE,wsn.network.NumberOfPacket);
        data.put(TopologyConstants.SENSORS_MIN_SENDING_RATE_KEY,"1");
        data.put(TopologyConstants.SENSORS_MIN_PROCESSING_RATE_KEY,"1");
        data.put(TopologyConstants.CHANNEL_MIN_SENDING_RATE_KEY,"1");
        HashMap<String ,String > energyRule = new HashMap<>();
        energyRule.put(EnergyConstants.PROCESSING_MESSAGE_ENERGY_CONSUMPTION_KEY , "5");
        energyRule.put(EnergyConstants.SENDING_MESSAGE_ENERGY_COMSUMPTION_KEY,"5");
        energyRule.put(EnergyConstants.RECEIVE_MESSAGE_ENERGY_COMSUMPTION_KEY,"5");

        data.put(TopologyConstants.ENERGY_RULES_LIST_KEY,energyRule);
        System.out.println(data);
        System.out.println("No problems then");
    }
}

/*public ArrayList<Object> readKwsn (String filePath) 
{
    try {
    	//File desktop = new File(System.getProperty("user.home"), "/Desktop"); 
    	//filePath = desktop + "/5-sensors.kwsn";
    	filePath = "C:/Users/Particle/Desktop/5-sensors.kwsn";
        JAXBContext context = JAXBContext.newInstance(Wsn.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Wsn wsn = (Wsn) unmarshaller.unmarshal(new File(filePath));

        ArrayList<Object> kwsnData = new ArrayList<>();
        processData(kwsnData,wsn);

        return kwsnData;

    } catch (JAXBException e) {
        e.printStackTrace();
        return null;
    }
}*/


