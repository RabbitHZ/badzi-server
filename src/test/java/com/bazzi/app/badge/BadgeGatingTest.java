package com.bazzi.app.badge;

import com.bazzi.app.application.service.shop.ShopService;
import com.bazzi.app.infrastructure.persistence.badge.BadgeStyle;
import com.bazzi.app.infrastructure.persistence.badge.BadgeStyleRepository;
import com.bazzi.app.infrastructure.persistence.shop.ShopItem;
import com.bazzi.app.util.jwt.JwtProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BadgeGatingTest {

    @Autowired MockMvc mockMvc;
    @Autowired JwtProvider jwtProvider;
    @MockBean ShopService shopService;
    @MockBean BadgeStyleRepository badgeStyleRepository;

    @Test
    void basic_스타일은_인증없이_200() throws Exception {
        mockMvc.perform(get("/api/badges")
                        .param("url", "https://github.com/test")
                        .param("styleType", "basic"))
                .andExpect(status().isOk());
    }

    @Test
    void preview는_인증없이_200() throws Exception {
        mockMvc.perform(get("/api/badges/preview")
                        .param("styleType", "maple"))
                .andExpect(status().isOk());
    }

    @Test
    void maple_미보유_인증있음_403() throws Exception {
        String token = jwtProvider.createAccessToken(1L);

        ShopItem shopItem = mock(ShopItem.class);
        when(shopItem.getId()).thenReturn(10L);

        BadgeStyle preset = mock(BadgeStyle.class);
        when(preset.getShopItem()).thenReturn(shopItem);
        when(badgeStyleRepository.findByStyleTypeAndPresetTrue("maple"))
                .thenReturn(Optional.of(preset));
        when(shopService.userOwnsItem(1L, 10L)).thenReturn(false);

        mockMvc.perform(get("/api/badges")
                        .header("Authorization", "Bearer " + token)
                        .param("url", "https://github.com/test")
                        .param("styleType", "maple"))
                .andExpect(status().isForbidden());
    }

    @Test
    void maple_보유중_인증있음_200() throws Exception {
        String token = jwtProvider.createAccessToken(1L);

        ShopItem shopItem = mock(ShopItem.class);
        when(shopItem.getId()).thenReturn(10L);

        BadgeStyle preset = mock(BadgeStyle.class);
        when(preset.getShopItem()).thenReturn(shopItem);
        when(badgeStyleRepository.findByStyleTypeAndPresetTrue("maple"))
                .thenReturn(Optional.of(preset));
        when(shopService.userOwnsItem(1L, 10L)).thenReturn(true);

        mockMvc.perform(get("/api/badges")
                        .header("Authorization", "Bearer " + token)
                        .param("url", "https://github.com/test")
                        .param("styleType", "maple"))
                .andExpect(status().isOk());
    }

    @Test
    void maple_인증없음_403() throws Exception {
        when(badgeStyleRepository.findByStyleTypeAndPresetTrue("maple"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/badges")
                        .param("url", "https://github.com/test")
                        .param("styleType", "maple"))
                .andExpect(status().isForbidden());
    }
}
