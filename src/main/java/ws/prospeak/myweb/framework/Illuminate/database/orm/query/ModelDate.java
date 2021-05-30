package ws.prospeak.myweb.framework.Illuminate.database.orm.query;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class ModelDate extends Date {
    private static final long serialVersionUID = -5908154631987653080L;

    @JsonIgnore
    ModelDate self = this;

}
