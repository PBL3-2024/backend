package io.github.pbl32024.model.analytics;

import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/*
 * This is probably overkill for our expected load, but it should make this very low latency
 */
@Slf4j
@Service
public class AnalyticsService {

	private static final int DISRUPTOR_BUFFER_SIZE = 1024;

	// This is thread-safe as long as it's only accessed by disruptor
	private final ArrayList<ClickData> synchronizationRequiredClickData = new ArrayList<>();

	private final AnalyticsDAO analyticsDAO;
	private final Disruptor<ClickData> disruptor;

	public AnalyticsService(AnalyticsDAO analyticsDAO) {
		this.analyticsDAO = analyticsDAO;

		disruptor = new Disruptor<>(ClickData::new, DISRUPTOR_BUFFER_SIZE, DaemonThreadFactory.INSTANCE);
		disruptor.handleEventsWith(this::handleClickData);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void start() {
		disruptor.start();
	}

	@EventListener(ContextClosedEvent.class)
	public void shutdown() {
		disruptor.shutdown();
	}

	public Report getUserCurrentOccupationReport(AnalyticsQuery query) {
		return null;
	}

	public Report getUserGoalOccupationReport(AnalyticsQuery query) {
		return null;
	}

	public Report getJobPostingEngagement(AnalyticsQuery query) {
		return null;
	}

	public Report getNewsEngagement(AnalyticsQuery query) {
		return null;
	}

	public Report getCertificationEngagement(AnalyticsQuery query) {
		return null;
	}

	public Report getLearningMaterialEngagement(AnalyticsQuery query) {
		return null;
	}

	public void getClickData(ClickDataQuery query, OutputStream body) {

	}

	public void saveClickData(ClickData data) {
		disruptor.publishEvent(this::translateClickData, data);
	}

	private void handleClickData(ClickData event, long sequence, boolean endOfBatch) {
		synchronizationRequiredClickData.add(event);

		if (endOfBatch) {
			try {
				analyticsDAO.saveClickData(synchronizationRequiredClickData);
			} catch (Exception e) {
				log.warn("Failed to persist click data", e);
			} finally {
				synchronizationRequiredClickData.clear();
			}
		}
	}

	private void translateClickData(ClickData buffered, long sequence, ClickData incoming) {
		buffered.setTimestamp(incoming.getTimestamp());
		buffered.setElementId(incoming.getElementId());
		buffered.setElementType(incoming.getElementType());
		buffered.setUserId(incoming.getUserId());
		buffered.setUserPostalCode(incoming.getUserPostalCode());
		buffered.setUserCurrentOccupation(incoming.getUserCurrentOccupation());
		buffered.setUserGoalOccupation(incoming.getUserGoalOccupation());
	}

}
