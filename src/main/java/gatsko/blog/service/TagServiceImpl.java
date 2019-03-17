package gatsko.blog.service;

import gatsko.blog.model.Tag;
import gatsko.blog.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("tagService")
public class TagServiceImpl implements TagService {
    @Autowired
    private TagRepository tagRepository;

    @Override
    public Tag saveTag(Tag tag) {
        if (tagRepository.findByName(tag.getName()) == null) {
            return tagRepository.saveAndFlush(tag);
        }
        else return tagRepository.findByName(tag.getName());
    }
}
