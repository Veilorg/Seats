/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.telemetry.ping;

import org.mozilla.telemetry.config.TelemetryConfiguration;
import org.mozilla.telemetry.measurement.CreatedTimestampMeasurement;
import org.mozilla.telemetry.measurement.EventsMeasurement;
import org.mozilla.telemetry.measurement.LocaleMeasurement;
import org.mozilla.telemetry.measurement.OperatingSystemMeasurement;
import org.mozilla.telemetry.measurement.OperatingSystemVersionMeasurement;
import org.mozilla.telemetry.measurement.SequenceMeasurement;
import org.mozilla.telemetry.measurement.SettingsMeasurement;
import org.mozilla.telemetry.measurement.TimezoneOffsetMeasurement;

/**
 * A telemetry ping builder for pings of type "focus-event".
 *
 * @deprecated prefer {@link TelemetryMobileEventPingBuilder}. "focus-event" was the original type but
 * we're migrating to the generic "mobile-event" type. In order to prevent breaking public APIs, we couldn't
 * change this class directly and copied it into the new ping type.
 */
@Deprecated
public class TelemetryEventPingBuilder extends TelemetryPingBuilder {
    public static final String TYPE = "focus-event";
    private static final int VERSION = 1;

    private EventsMeasurement eventsMeasurement;

    public TelemetryEventPingBuilder(TelemetryConfiguration configuration) {
        super(configuration, TYPE, VERSION);

        addMeasurement(new SequenceMeasurement(configuration, this));
        addMeasurement(new LocaleMeasurement());
        addMeasurement(new OperatingSystemMeasurement());
        addMeasurement(new OperatingSystemVersionMeasurement());
        addMeasurement(new CreatedTimestampMeasurement());
        addMeasurement(new TimezoneOffsetMeasurement());
        addMeasurement(new SettingsMeasurement(configuration));
        addMeasurement(eventsMeasurement = new EventsMeasurement(configuration));
    }

    public EventsMeasurement getEventsMeasurement() {
        return eventsMeasurement;
    }

    @Override
    public boolean canBuild() {
        return eventsMeasurement.getEventCount() >= getConfiguration().getMinimumEventsForUpload();
    }

    @Override
    protected String getUploadPath(final String documentId) {
        return super.getUploadPath(documentId) + "?v=4";
    }
}
