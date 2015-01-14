package kr.ac.kaist.kse.cc.heartratemonitor;

import java.util.ArrayList;
import java.util.List;

import kr.ac.kaist.kse.cc.heartratemonitor.infrastructure.NetworkKeys;

import org.apache.log4j.Logger;
import org.cowboycoders.ant.Channel;
import org.cowboycoders.ant.Node;
import org.cowboycoders.ant.events.BroadcastListener;
import org.cowboycoders.ant.interfaces.AntTransceiver;
import org.cowboycoders.ant.messages.ChannelType;
import org.cowboycoders.ant.messages.SlaveChannelType;
import org.cowboycoders.ant.messages.data.BroadcastDataMessage;

public class HeartRateMonitor {

	private static class Listener implements
			BroadcastListener<BroadcastDataMessage> {

		private String id;

		public Listener(String hrmId) {
			this.id = hrmId;
		}

		public void receiveMessage(BroadcastDataMessage message) {
			System.out.print(message.getUnsignedData()[7] + "\t" + id + "\t");
			for (int b : message.getUnsignedData()) {
				String value = "";
				if (b < 10)
					value = "__";
				else if (b < 100)
					value = "_";
				System.out.print("[" + value + b + "]");
			}
			System.out.println();
			String log = "" + id;
			for (int b : message.getUnsignedData())
				log += ("," + b);
			writeLog(log);
		}

	}

	private static final int HRM_CHANNEL_PERIOD = 8070;
	private static final int HRM_CHANNEL_FREQ = 57;
	private static final boolean HRM_PAIRING_FLAG = false;
	private static final int HRM_DEVICE_TYPE = 120;

	private static Logger log = Logger.getLogger("HeartRate");

	private static Node node;

	public HeartRateMonitor() {
		AntTransceiver antchip = new AntTransceiver(0);
		node = new Node(antchip);
		node.start();
		node.reset();
	}

	private static void writeLog(String l) {
		log.info(System.currentTimeMillis() + "," + l);
	}

	boolean working = true;

	public void run() {
		List<Channel> channels = new ArrayList<Channel>();

		/*Channel motorolaChannel = createHeartRateMeasuringDeviceChannel(1,
				22117, "MOTOROLA");
		channels.add(motorolaChannel);
		motorolaChannel.open();*/

		Channel mioLinkChannel = createHeartRateMeasuringDeviceChannel(1,
				35714, "MIO Link");
		channels.add(mioLinkChannel);
		mioLinkChannel.open();

		while (working)
			;

		//getNode().freeChannel(motorolaChannel);
		getNode().freeChannel(mioLinkChannel);

		getNode().stop();

	}

	private Node getNode() {
		return node;
	}

	private Channel createHeartRateMeasuringDeviceChannel(int transmissionType,
			int id, String nameOfDevice) {
		Channel channel = getNode().getFreeChannel();
		channel.setName(nameOfDevice);
		ChannelType channelType = new SlaveChannelType();
		channel.assign(NetworkKeys.ANT_SPORT, channelType);
		channel.registerRxListener(new Listener(nameOfDevice),
				BroadcastDataMessage.class);
		channel.setId(id, HRM_DEVICE_TYPE, transmissionType, HRM_PAIRING_FLAG);
		channel.setFrequency(HRM_CHANNEL_FREQ);
		channel.setPeriod(HRM_CHANNEL_PERIOD);
		channel.setSearchTimeout(Channel.SEARCH_TIMEOUT_NEVER);
		return channel;

	}

	public static void main(String[] args) throws InterruptedException {
		HeartRateMonitor a = new HeartRateMonitor();
		a.run();

	}

}
