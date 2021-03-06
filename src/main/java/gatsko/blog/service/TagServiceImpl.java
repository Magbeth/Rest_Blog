package gatsko.blog.service;

import gatsko.blog.model.Tag;
import gatsko.blog.repository.TagRepository;
import gatsko.blog.service.apiInterface.TagService;
import org.springframework.stereotype.Service;

@Service("tagService")
public class TagServiceImpl implements TagService {
    private TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Tag saveTag(Tag tag) {
        if (!tagRepository.existsByName(tag.getName())) {
            return tagRepository.saveAndFlush(tag);
        }
        return tagRepository.findByName(tag.getName());
    }
}
