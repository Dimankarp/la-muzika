package modernovo.muzika.model.dto;

import java.util.List;

public class MusicBandBatchDTO {

    private List<MusicBandDTO> bands;

    public List<MusicBandDTO> getBands() {
        return bands;
    }

    public void setBands(List<MusicBandDTO> bands) {
        this.bands = bands;
    }
}
