package gatsko.blog.service;

import gatsko.blog.model.Tag;
import gatsko.blog.repository.TagRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TagServiceImplTest {
    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagServiceImpl sut;

    @Test
    public void saveTag() {
        Tag tag = new Tag();
        tag.setName("tagName");
        when(tagRepository.existsByName(tag.getName())).thenReturn(false);
        sut.saveTag(tag);
        verify(tagRepository).existsByName(anyString());
        verify(tagRepository).saveAndFlush(tag);
    }
}