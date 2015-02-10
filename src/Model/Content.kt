package org.jetbrains.dokka

import kotlin.properties.Delegates

public abstract class ContentNode {
    class object {
        val empty = ContentEmpty
    }
}

public object ContentEmpty : ContentNode()

public open class ContentBlock() : ContentNode() {
    val children = arrayListOf<ContentNode>()

    fun append(node : ContentNode)  {
        children.add(node)
    }

    fun isEmpty() = children.isEmpty()
}

public data class ContentText(val text: String) : ContentNode()
public data class ContentKeyword(val text: String) : ContentNode()
public data class ContentIdentifier(val text: String) : ContentNode()
public data class ContentSymbol(val text: String) : ContentNode()

public class ContentParagraph() : ContentBlock()
public class ContentEmphasis() : ContentBlock()
public class ContentStrong() : ContentBlock()
public class ContentStrikethrough() : ContentBlock()
public class ContentCode() : ContentBlock()
public class ContentBlockCode() : ContentBlock()
public class ContentNodeLink(val node : DocumentationNode) : ContentBlock()
public class ContentExternalLink(val href : String) : ContentBlock()
public class ContentList() : ContentBlock()
public class ContentListItem() : ContentBlock()
public class ContentSection(public val tag: String, public val subjectName: String?) : ContentBlock()

fun content(body: ContentBlock.() -> Unit): ContentBlock {
    val block = ContentBlock()
    block.body()
    return block
}

fun ContentBlock.text(value: String) = append(ContentText(value))
fun ContentBlock.keyword(value: String) = append(ContentKeyword(value))
fun ContentBlock.symbol(value: String) = append(ContentSymbol(value))
fun ContentBlock.identifier(value: String) = append(ContentIdentifier(value))

fun ContentBlock.link(to: DocumentationNode, body: ContentNode.() -> Unit) {
    val block = ContentNodeLink(to)
    block.body()
    append(block)
}

public class Content() : ContentBlock() {
    private val sectionList = arrayListOf<ContentSection>()
    public val sections: List<ContentSection>
        get() = sectionList

    fun addSection(tag: String?, subjectName: String?): ContentSection {
        val section = ContentSection(tag ?: "", subjectName)
        sectionList.add(section)
        return section
    }

    fun findSectionByTag(tag: String): ContentSection? =
        sections.firstOrNull { tag.equalsIgnoreCase(it.tag) }

    public val summary: ContentNode get() = children.firstOrNull() ?: ContentEmpty

    public val description: ContentNode by Delegates.lazy {
        val descriptionNodes = children.drop(1)
        if (descriptionNodes.isEmpty()) {
            ContentEmpty
        } else {
            val result = ContentSection("Description", null)
            result.children.addAll(descriptionNodes)
            result
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Content)
            return false
        return sections == other.sections && children == other.children
    }

    override fun hashCode(): Int {
        return sections.map { it.hashCode() }.sum()
    }

    override fun toString(): String {
        if (sections.isEmpty())
            return "<empty>"
        return (listOf(summary, description) + sections).joinToString()
    }

    val isEmpty: Boolean
        get() = sections.none()

    class object {
        val Empty = Content()
    }
}
