<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : map2properties.xsl
    Created on : December 10, 2014, 12:27 PM
    Author     : Peter Withers <p.withers@psych.ru.nl>
    Description:
        Converts the SVG map from inkscape into a properties file with all the regions from the input SVG.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:svg="http://www.w3.org/2000/svg"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xmlns="http://www.w3.org/2000/svg"
                xmlns:inkscape="http://www.inkscape.org/namespaces/inkscape"
                version="2.0">
    <xsl:output method="text" encoding="UTF-8" />
    <xsl:template match="/">
        <xsl:text># generated with map2properties.xsls
</xsl:text>
        <xsl:for-each select="svg:svg/svg:g[svg:path]">
            <xsl:text>style</xsl:text>
            <xsl:value-of select="replace(@inkscape:label, ' ', '_')"/>
            <xsl:text>=</xsl:text>
            <xsl:value-of select="svg:path/@style"/>
            <xsl:text>
</xsl:text>
            <xsl:text>data</xsl:text>
            <xsl:value-of select="replace(@inkscape:label, ' ', '_')"/>
            <xsl:text>=</xsl:text>
            <xsl:value-of select="svg:path/@d"/>
            <xsl:text>
</xsl:text>
            <xsl:text>id</xsl:text>
            <xsl:value-of select="replace(@inkscape:label, ' ', '_')"/>
            <!--<xsl:value-of select="@inkscape:label"/>-->
            <xsl:text>=</xsl:text>
            <xsl:value-of select="replace(@inkscape:label, ' ', '_')"/>
            <xsl:text>
</xsl:text> 
        </xsl:for-each>
        <xsl:for-each select="svg:svg/svg:g[svg:path]">
#        builder.append(SVG_TEMPLATE.pathTag(autotypRegions.id<xsl:value-of select="replace(@inkscape:label, ' ', '_')"/>(), autotypRegions.style<xsl:value-of select="replace(@inkscape:label, ' ', '_')"/>(), autotypRegions.data<xsl:value-of select="replace(@inkscape:label, ' ', '_')"/>()));
        </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>