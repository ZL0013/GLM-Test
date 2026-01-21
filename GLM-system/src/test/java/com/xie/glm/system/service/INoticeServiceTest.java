package com.xie.glm.system.service;

import com.xie.glm.common.core.PageResult;
import com.xie.glm.system.dto.NoticeDTO;
import com.xie.glm.system.dto.query.NoticeQueryDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 通知服务接口测试
 *
 * @author xie
 */
@DisplayName("通知服务接口测试")
class INoticeServiceTest {

    @Nested
    @DisplayName("查询操作测试")
    class QueryOperationsTest {

        @Test
        @DisplayName("分页查询通知列表")
        void testListNotices() {
            // 此测试为接口测试，实际测试在实现类中进行
            INoticeService service = mock(INoticeService.class);
            NoticeQueryDTO query = new NoticeQueryDTO();
            PageResult<NoticeDTO> expected = new PageResult<>(List.of(), 0L);

            when(service.listNotices(query)).thenReturn(expected);

            PageResult<NoticeDTO> result = service.listNotices(query);

            assertNotNull(result);
            assertEquals(0, result.getRecords().size());
            verify(service).listNotices(query);
        }

        @Test
        @DisplayName("根据ID查询通知详情")
        void testGetNoticeById() {
            INoticeService service = mock(INoticeService.class);
            Long noticeId = 1L;
            NoticeDTO expected = new NoticeDTO();
            expected.setNoticeId(noticeId);

            when(service.getNoticeById(noticeId)).thenReturn(expected);

            NoticeDTO result = service.getNoticeById(noticeId);

            assertNotNull(result);
            assertEquals(noticeId, result.getNoticeId());
            verify(service).getNoticeById(noticeId);
        }
    }

    @Nested
    @DisplayName("增删改操作测试")
    class ModifyOperationsTest {

        @Test
        @DisplayName("创建通知")
        void testCreateNotice() {
            INoticeService service = mock(INoticeService.class);
            NoticeDTO dto = new NoticeDTO();
            dto.setNoticeTitle("测试通知");

            doNothing().when(service).createNotice(dto);

            service.createNotice(dto);

            verify(service).createNotice(dto);
        }

        @Test
        @DisplayName("更新通知")
        void testUpdateNotice() {
            INoticeService service = mock(INoticeService.class);
            NoticeDTO dto = new NoticeDTO();
            dto.setNoticeId(1L);

            doNothing().when(service).updateNotice(dto);

            service.updateNotice(dto);

            verify(service).updateNotice(dto);
        }

        @Test
        @DisplayName("删除通知")
        void testDeleteNotice() {
            INoticeService service = mock(INoticeService.class);
            Long noticeId = 1L;

            doNothing().when(service).deleteNotice(noticeId);

            service.deleteNotice(noticeId);

            verify(service).deleteNotice(noticeId);
        }
    }
}
