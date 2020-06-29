package javadoc.signatures

import javadoc.translators.documentables.JavadocPageContentBuilder
import org.jetbrains.dokka.base.signatures.JvmSignatureUtils
import org.jetbrains.dokka.base.signatures.SignatureProvider
import org.jetbrains.dokka.base.transformers.pages.comments.CommentsToContentConverter
import org.jetbrains.dokka.kotlinAsJava.signatures.JavaSignatureUtils
import org.jetbrains.dokka.model.*
import org.jetbrains.dokka.model.properties.PropertyContainer
import org.jetbrains.dokka.pages.ContentKind
import org.jetbrains.dokka.pages.ContentNode
import org.jetbrains.dokka.utilities.DokkaLogger

class JavadocSignatureProvider(ctcc: CommentsToContentConverter, logger: DokkaLogger) : SignatureProvider,
    JvmSignatureUtils by JavaSignatureUtils {

    private val contentBuilder = JavadocPageContentBuilder(ctcc, this, logger)

    override fun signature(documentable: Documentable): List<ContentNode> = when (documentable) {
        is DFunction -> signature(documentable)
        is DProperty -> signature(documentable)
        is DClasslike -> signature(documentable)
        is DEnumEntry -> signature(documentable)
        is DTypeParameter -> signature(documentable)
        else -> throw NotImplementedError(
            "Cannot generate signature for ${documentable::class.qualifiedName} ${documentable.name}"
        )
    }

    private fun signature(c: DClasslike): List<ContentNode> =
        javadocSignature(c) {
            annotations {
                text("annotations")
            }
            modifiers {
                text("modifiers")
            }
            signatureWithoutModifiers {
                text("signature")
            }
        }

    private fun signature(f: DFunction): List<ContentNode> =
        javadocSignature(f) {
            annotations {
                text("annotations")
            }
            modifiers {
                text("modifiers")
            }
            signatureWithoutModifiers {
                text("signature")
            }
        }

    private fun signature(p: DProperty): List<ContentNode> =
        javadocSignature(p) {
            annotations {
                text("annotations")
            }
            modifiers {
                text("modifiers")
            }
            signatureWithoutModifiers {
                text("signature")
            }
        }

    private fun signature(e: DEnumEntry): List<ContentNode> =
        javadocSignature(e) {
            annotations {
                text("annotations")
            }
            modifiers {
                text("modifiers")
            }
            signatureWithoutModifiers {
                text("signature")
            }
        }

    private fun signature(t: DTypeParameter): List<ContentNode> =
        javadocSignature(t) {
            annotations {
                text("annotations")
            }
            modifiers {
                text("modifiers")
            }
            signatureWithoutModifiers {
                text("signature")
            }
        }

    private fun javadocSignature(
        d: Documentable,
        extra: PropertyContainer<ContentNode> = PropertyContainer.empty(),
        block: JavadocPageContentBuilder.JavadocContentBuilder.() -> Unit
    ): List<ContentNode> =
        d.sourceSets.map {
            contentBuilder.contentFor(d, ContentKind.Main) {
                with(contentBuilder) {
                    javadocGroup(d.dri, d.sourceSets, extra, block)
                }
            }
        }
}
