/*
 * Copyright 2014-2024 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

import React from 'react';
import { chunk } from '../utils/arrayUtils';
import { OptionWithSearchResult, SearchProps } from './types';

type HighlighterProps = {
  label: string;
};

const Highlighter: React.FC<HighlighterProps> = ({ label }: HighlighterProps) => {
  return <strong>{label}</strong>;
};

export const signatureFromSearchResult = (searchResult: OptionWithSearchResult): string => {
  return searchResult.name.replace(searchResult.searchKeys[searchResult.rank], searchResult.highlight);
};

export const SearchResultRow: React.FC<SearchProps> = ({ searchResult }: SearchProps) => {
  /* This is a work-around for an issue: https://youtrack.jetbrains.com/issue/RG-2108 */
  const out = chunk(signatureFromSearchResult(searchResult).split('**'), 2).flatMap(([txt, label], index) => [
    txt,
    label ? <Highlighter label={label} key={index} /> : null,
  ]);

  return (
    <div className="template-wrapper">
      <div className="template-title">{out}</div>
      <span className="template-description">{searchResult.description}</span>
    </div>
  );
};
