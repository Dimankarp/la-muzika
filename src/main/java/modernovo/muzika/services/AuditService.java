package modernovo.muzika.services;

import modernovo.muzika.model.ActionType;
import modernovo.muzika.model.AuditEntry;
import modernovo.muzika.model.MusicBand;
import modernovo.muzika.model.User;
import modernovo.muzika.repositories.AuditEntryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuditService {
    private final Logger logger = LoggerFactory.getLogger(BandService.class);
    private final AuditEntryRepository auditEntryRepository;

    public AuditService(AuditEntryRepository auditEntryRepository) {
        this.auditEntryRepository = auditEntryRepository;
    }

    public void addEntry(User creator, MusicBand target, ActionType action){
        var entry = new AuditEntry(creator, target, action);
        logger.debug("Adding new entry {}", entry);
        auditEntryRepository.save(entry);
    }

}
