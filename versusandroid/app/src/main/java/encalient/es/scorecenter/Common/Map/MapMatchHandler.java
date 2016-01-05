package encalient.es.scorecenter.Common.Map;

import es.encalient.ProtoMatchDTO;
import es.encalient.ProtoWeekDTO;

/**
 * Created by EnCalientes on 10/7/2015.
 */
public interface MapMatchHandler {
    void setMap(ProtoMatchDTO.MatchDTO matchDTO, ProtoWeekDTO.WeekDTO week);
}
