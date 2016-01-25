package org.jetbrains.dokka.javadoc

import org.jetbrains.dokka.DokkaConsoleLogger
import org.jetbrains.dokka.tests.verifyModel
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue

class JavadocTest {
    @Test fun testTypes() {
        verifyModel("testdata/javadoc/types.kt", format = "javadoc", withJdk = true) { model ->
            val doc = ModuleNodeAdapter(model, StandardReporter(DokkaConsoleLogger), "")
            val classDoc = doc.classNamed("foo.TypesKt")!!
            val method = classDoc.methods().find { it.name() == "foo" }!!

            val type = method.returnType()
            assertFalse(type.asClassDoc().isIncluded)
            assertEquals("java.lang.String", type.qualifiedTypeName())
            assertEquals("java.lang.String", type.asClassDoc().qualifiedName())

            val params = method.parameters()
            assertTrue(params[0].type().isPrimitive)
            assertFalse(params[1].type().asClassDoc().isIncluded)
        }
    }

    @Test fun testObject() {
        verifyModel("testdata/javadoc/obj.kt", format = "javadoc") { model ->
            val doc = ModuleNodeAdapter(model, StandardReporter(DokkaConsoleLogger), "")

            val classDoc = doc.classNamed("foo.O")
            assertNotNull(classDoc)

            val companionDoc = doc.classNamed("foo.O.Companion")
            assertNotNull(companionDoc)

            val pkgDoc = doc.packageNamed("foo")!!
            assertEquals(2, pkgDoc.allClasses().size)
        }
    }

}
