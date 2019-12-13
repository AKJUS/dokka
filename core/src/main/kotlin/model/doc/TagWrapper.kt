package org.jetbrains.dokka.model.doc

sealed class TagWrapper(val root: DocTag)
class Description(root: DocTag) : TagWrapper(root)
class Author(root: DocTag) : TagWrapper(root)
class Version(root: DocTag) : TagWrapper(root)
class Since(root: DocTag) : TagWrapper(root)
class See(root: DocTag, val name: String) : TagWrapper(root)
class Param(root: DocTag, val name: String) : TagWrapper(root)
class Return(root: DocTag) : TagWrapper(root)
class Receiver(root: DocTag) : TagWrapper(root)
class Constructor(root: DocTag) : TagWrapper(root)
class Throws(root: DocTag, val name: String) : TagWrapper(root)
class Sample(root: DocTag, val name: String) : TagWrapper(root)
class Deprecated(root: DocTag) : TagWrapper(root)
class Property(root: DocTag, val name: String) : TagWrapper(root)
class Suppress(root: DocTag) : TagWrapper(root)
class CustomWrapperTag(root: DocTag, val name: String) : TagWrapper(root)