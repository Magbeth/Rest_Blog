package gatsko.blog.service;

import gatsko.blog.model.Tag;
import gatsko.blog.repository.TagRepository;
import org.springframework.stereotype.Service;

@Service("tagService")
public class TagServiceImpl implements TagService {
    private TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Tag saveTag(Tag tag) {
        if (tagRepository.findByName(tag.getName()) == null) {
            return tagRepository.saveAndFlush(tag);
        }
        else return tagRepository.findByName(tag.getName());
    }
}
