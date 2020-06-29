package javadoc.translators.documentables

import javadoc.pages.JavadocSignatureContentNode
import org.jetbrains.dokka.DokkaConfiguration
import org.jetbrains.dokka.base.signatures.SignatureProvider
import org.jetbrains.dokka.base.transformers.pages.comments.CommentsToContentConverter
import org.jetbrains.dokka.base.translators.documentables.PageContentBuilder
import org.jetbrains.dokka.links.DRI
import org.jetbrains.dokka.model.properties.PropertyContainer
import org.jetbrains.dokka.pages.ContentKind
import org.jetbrains.dokka.pages.ContentNode
import org.jetbrains.dokka.utilities.DokkaLogger

class JavadocPageContentBuilder(
    commentsConverter: CommentsToContentConverter,
    signatureProvider: SignatureProvider,
    logger: DokkaLogger
) : PageContentBuilder(commentsConverter, signatureProvider, logger) {

    fun PageContentBuilder.DocumentableContentBuilder.javadocGroup(
        dri: DRI = mainDRI.first(),
        sourceSets: Set<DokkaConfiguration.DokkaSourceSet> = mainSourcesetData,
        extra: PropertyContainer<ContentNode> = mainExtra,
        block: JavadocContentBuilder.() -> Unit
    ) {
        +JavadocContentBuilder(
            mainDri = dri,
            mainExtra = extra,
            mainSourceSet = sourceSets,
        ).apply(block).build()
    }

    open inner class JavadocContentBuilder(
        private val mainDri: DRI,
        private val mainExtra: PropertyContainer<ContentNode>,
        private val mainSourceSet: Set<DokkaConfiguration.DokkaSourceSet>,
    ) {
        var annotations: ContentNode? = null
        var modifiers: ContentNode? = null
        var signatureWithoutModifiers: ContentNode? = null

        fun annotations(block: PageContentBuilder.DocumentableContentBuilder.() -> Unit) {
            annotations = buildContentForBlock(block)
        }

        fun modifiers(block: PageContentBuilder.DocumentableContentBuilder.() -> Unit) {
            modifiers = buildContentForBlock(block)
        }

        fun signatureWithoutModifiers(block: PageContentBuilder.DocumentableContentBuilder.() -> Unit) {
            signatureWithoutModifiers = buildContentForBlock(block)
        }

        private fun buildContentForBlock(block: PageContentBuilder.DocumentableContentBuilder.() -> Unit) =
            contentFor(
                dri = mainDri,
                sourceSets = mainSourceSet,
                kind = ContentKind.Symbol,
                extra = mainExtra,
                block = block
            )

        fun build(
            dri: DRI = mainDri,
            sourceSets: Set<DokkaConfiguration.DokkaSourceSet> = mainSourceSet,
            extra: PropertyContainer<ContentNode> = PropertyContainer.empty()
        ): JavadocSignatureContentNode = JavadocSignatureContentNode(
            dri = dri,
            annotations = annotations,
            modifiers = modifiers!!,
            signatureWithoutModifiers = signatureWithoutModifiers!!
        )
    }
}