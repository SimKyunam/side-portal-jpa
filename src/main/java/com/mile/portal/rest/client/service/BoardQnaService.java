package com.mile.portal.rest.client.service;

import com.mile.portal.config.cache.CacheProperties;
import com.mile.portal.config.exception.exceptions.ResultNotFoundException;
import com.mile.portal.rest.base.service.BaseBoardQnaService;
import com.mile.portal.rest.client.repository.ClientRepository;
import com.mile.portal.rest.common.model.comm.ReqBoard;
import com.mile.portal.rest.common.model.domain.Code;
import com.mile.portal.rest.common.model.domain.board.BoardQna;
import com.mile.portal.rest.common.repository.BoardQnaRepository;
import com.mile.portal.rest.common.service.BoardAttachService;
import com.mile.portal.rest.common.service.CodeService;
import com.mile.portal.rest.mng.repository.ManagerQnaRepository;
import com.mile.portal.util.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@Transactional
public class BoardQnaService extends BaseBoardQnaService {
    private final BoardAttachService boardAttachService;

    private final BoardQnaRepository boardQnaRepository;
    private final ClientRepository clientRepository;
    private final ManagerQnaRepository managerQnaRepository;

    private final MailUtil mailUtil;

    public BoardQnaService(CodeService codeService,
                           BoardAttachService boardAttachService,
                           BoardQnaRepository boardQnaRepository,
                           ClientRepository clientRepository,
                           ManagerQnaRepository managerQnaRepository,
                           MailUtil mailUtil) {
        super(codeService, boardAttachService, boardQnaRepository);
        this.boardAttachService = boardAttachService;
        this.boardQnaRepository = boardQnaRepository;
        this.clientRepository = clientRepository;
        this.managerQnaRepository = managerQnaRepository;
        this.mailUtil = mailUtil;
    }

    @CacheEvict(value = CacheProperties.BOARD_QNA, allEntries = true)
    public BoardQna createBoardQna(ReqBoard.BoardQna reqBoardQna, List<MultipartFile> files, Long clientId) {
        BoardQna boardQna = BoardQna.builder()
                .title(reqBoardQna.getTitle())
                .content(reqBoardQna.getContent())
                .qnaType(reqBoardQna.getQnaType())
                .client(clientRepository.findById(clientId).orElseThrow(ResultNotFoundException::new))
                .build();

        BoardQna qna = boardQnaRepository.save(boardQna);

        //첨부파일 저장
        boardAttachService.boardAttachCheckAndUseUuidProcess(qna, "QNA", files);

        //문의내역 담당자 메일 발송
        Code code = selectCode("qnaType").get(reqBoardQna.getQnaType()); //공통코드에서 이름 가져오기
        AtomicInteger indexCnt = new AtomicInteger();
        managerQnaRepository.findAllByQnaType(reqBoardQna.getQnaType())
                .forEach(managerQna -> {
                    int index = indexCnt.incrementAndGet();
                    sendEmail(managerQna.getEmail(), qna.getId(), reqBoardQna.getTitle(), reqBoardQna.getContent(), code.getCodeName(), index);
                });

        return qna;
    }

    @CacheEvict(value = CacheProperties.BOARD_QNA, allEntries = true)
    public BoardQna updateBoardQna(ReqBoard.BoardQna reqBoardQna, List<MultipartFile> files, Long clientId) {
        BoardQna boardQna = boardQnaRepository.findById(reqBoardQna.getId()).orElseThrow(ResultNotFoundException::new);

        boardQna.setTitle(reqBoardQna.getTitle());
        boardQna.setContent(reqBoardQna.getContent());
        boardQna.setQnaType(reqBoardQna.getQnaType());
        boardQna.setClient(clientRepository.findById(clientId).orElseThrow(ResultNotFoundException::new));

        BoardQna qna = boardQnaRepository.save(boardQna);

        //첨부파일을 수정한 경우
        if (reqBoardQna.getFileModifiedYn().equals("Y")) {
            boardAttachService.deleteBoardAttachFile(qna, "QNA", reqBoardQna.getDeleteUploadNames());
            boardAttachService.boardAttachCheckAndUseUuidProcess(qna, "QNA", files);
        }

        return qna;
    }

    @Override
    public List<BoardQna> boardDeleteProc(List<Long> boardIdList, Long clientId) {
        List<BoardQna> boardQnas = boardQnaRepository.findByIdInAndClientId(boardIdList, clientId);
        boardAttachService.deleteBoardAttachFiles(boardQnas, "QNA"); // 첨부파일 삭제
        return boardQnas;
    }

    public void sendEmail(String email, Long id, String title, String content, String codeName, int index) {
        try {
            if (index > 1) {
                Thread.sleep(200);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Map<String, Object> mailProperty = new HashMap<>();
        mailProperty.put("id", id);
        mailProperty.put("title", title);
        mailProperty.put("content", content);
        mailProperty.put("codeName", codeName);

        mailUtil.sendTemplateMail(email, "[ 문의내역: " + title + "]", "포털 관리자", mailProperty, "mail/qna");
    }
}
