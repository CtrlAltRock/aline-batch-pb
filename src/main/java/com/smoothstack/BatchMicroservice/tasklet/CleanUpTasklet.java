package com.smoothstack.BatchMicroservice.tasklet;

import com.smoothstack.BatchMicroservice.maps.*;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class CleanUpTasklet implements Tasklet {
    private TransactionMap tMap = TransactionMap.getInstance();
    private MerchantMap mMap = MerchantMap.getInstance();
    private StateMap sMap = StateMap.getInstance();
    private LocationMap lMap = LocationMap.getInstance();
    private UserMap uMap = UserMap.getInstance();
    private CardMap cMap = CardMap.getInstance();
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        tMap.clearAll();
        mMap.clearAll();
        sMap.clearAll();
        lMap.clearAll();
        uMap.clearAll();
        cMap.clearAll();
        return null;
    }
}
