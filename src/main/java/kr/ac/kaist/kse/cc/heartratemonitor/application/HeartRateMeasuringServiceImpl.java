package kr.ac.kaist.kse.cc.heartratemonitor.application;

import kr.ac.kaist.kse.cc.heartratemonitor.domain.service.HeartRateMeasuringService;
import kr.ac.kaist.kse.cc.heartratemonitor.infrastructure.NetworkKeys;

import org.apache.log4j.Logger;
import org.cowboycoders.ant.Channel;
import org.cowboycoders.ant.Node;
import org.cowboycoders.ant.events.BroadcastListener;
import org.cowboycoders.ant.interfaces.AntTransceiver;
import org.cowboycoders.ant.messages.ChannelType;
import org.cowboycoders.ant.messages.SlaveChannelType;
import org.cowboycoders.ant.messages.data.BroadcastDataMessage;

public class HeartRateMeasuringServiceImpl implements HeartRateMeasuringService {

	private static final int HRM_CHANNEL_PERIOD = 8070;
	private static final int HRM_CHANNEL_FREQ = 57;
	private static final boolean HRM_PAIRING_FLAG = false;
	private static final int HRM_DEVICE_TYPE = 120;

	private static Logger log = Logger.getLogger("HeartRate");

	private static class Listener implements
			BroadcastListener<BroadcastDataMessage> {

		private String id;

		public Listener(String hrmId) {
			this.id = hrmId;
		}

		public void receiveMessage(BroadcastDataMessage message) {
			System.out.print(id + "," + System.currentTimeMillis() + ",");
			String csvOutput = "";
			for (int b : message.getUnsignedData()) {
				String value = "";
				if (b < 10)
					value = "__";
				else if (b < 100)
					value = "_";
				csvOutput += "," + b;
				System.out.print("[" + value + b + "]");
			}
			System.out.println();

			log.info(csvOutput);
		}
	}

	private static Node node;

	private boolean isRunning;

	private Channel channel;

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

	public boolean isRunning() {
		return isRunning;
	}

	private Node getNode() {
		if (node == null) {
			AntTransceiver antchip = new AntTransceiver(0);
			node = new Node(antchip);
			node.start();
			node.reset();
		}
		return node;
	}

	public void stop() {
		if (channel != null) {
			getNode().freeChannel(channel);
			channel = null;
		}
		if (isRunning) {
			getNode().stop();
			isRunning = false;
		}
	}

	public void start() {
		channel = createHeartRateMeasuringDeviceChannel(1, 35714, "MIO Link");
		channel.open();
		isRunning = true;
		Thread t = new Thread(new Runnable() {
			public void run() {
				while (isRunning)
					;
			}
		});
		t.start();
	}
}
