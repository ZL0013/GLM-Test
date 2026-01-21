package com.xie.glm.admin.facade;

import com.xie.glm.admin.converter.NoticeVoConverter;
import com.xie.glm.common.core.PageResult;
import com.xie.glm.system.dto.NoticeDTO;
import com.xie.glm.system.dto.query.NoticeQueryDTO;
import com.xie.glm.system.service.INoticeService;
import com.xie.glm.admin.vo.NoticeVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 通知门面测试
 *
 * @author xie
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("通知门面测试")
class NoticeFacadeTest {

    @Mock
    private INoticeService noticeService;

    @Mock
    private NoticeVoConverter noticeVoConverter;

    @InjectMocks
    private NoticeFacade noticeFacade;

    @Nested
    @DisplayName("查询操作测试")
    class QueryOperationsTest {

        @Test
        @DisplayName("分页查询通知列表")
        void testListNotices() {
            NoticeQueryDTO query = new NoticeQueryDTO();
            PageResult<NoticeDTO> dtoPage = new PageResult<>(List.of(), 0L);
            PageResult<NoticeVO> voPage = new PageResult<>(List.of(), 0L);

            when(noticeService.listNotices(query)).thenReturn(dtoPage);
            when(noticeVoConverter.toVoList(List.of())).thenReturn(List.of());

            PageResult<NoticeVO> result = noticeFacade.listNotices(query);

            assertNotNull(result);
            assertEquals(0, result.getRecords().size());
            verify(noticeService).listNotices(query);
        }

        @Test
        @DisplayName("根据ID查询通知详情")
        void testGetNoticeById() {
            Long noticeId = 1L;
            NoticeDTO dto = new NoticeDTO();
            dto.setNoticeId(noticeId);

            NoticeVO vo = new NoticeVO();
            vo.setNoticeId(noticeId);

            when(noticeService.getNoticeById(noticeId)).thenReturn(dto);
            when(noticeVoConverter.toVo(dto)).thenReturn(vo);

            NoticeVO result = noticeFacade.getNoticeById(noticeId);

            assertNotNull(result);
            assertEquals(noticeId, result.getNoticeId());
            verify(noticeService).getNoticeById(noticeId);
        }
    }

    @Nested
    @DisplayName("增删改操作测试")
    class ModifyOperationsTest {

        @Test
        @DisplayName("创建通知")
        void testCreateNotice() {
            NoticeDTO dto = new NoticeDTO();
            dto.setNoticeTitle("测试通知");

            doNothing().when(noticeService).createNotice(any(NoticeDTO.class));

            noticeFacade.createNotice(dto);

            verify(noticeService).createNotice(dto);
        }

        @Test
        @DisplayName("更新通知")
        void testUpdateNotice() {
            NoticeDTO dto = new NoticeDTO();
            dto.setNoticeId(1L);

            doNothing().when(noticeService).updateNotice(any(NoticeDTO.class));

            noticeFacade.updateNotice(dto);

            verify(noticeService).updateNotice(dto);
        }

        @Test
        @DisplayName("删除通知")
        void testDeleteNotice() {
            Long noticeId = 1L;

            doNothing().when(noticeService).deleteNotice(noticeId);

            noticeFacade.deleteNotice(noticeId);

            verify(noticeService).deleteNotice(noticeId);
        }
    }
}
